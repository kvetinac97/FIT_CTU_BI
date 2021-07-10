//
//  TableEditViewModel.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import SwiftUI
import FirebaseDatabase
import CodableFirebase

final class TableEditViewModel: ViewModel, ObservableObject {
    
    @Published var food = [Int: FoodOrder]()
    @Published var firstLoaded = false
    
    private var databaseReference: DatabaseReference!
    private var originalFood = [Int: FoodOrder]()
    
    private var firstTaskResult: [Food]? = nil
    private var secondTaskResult: [FoodOrder]? = nil
    
    init () {
        databaseReference = Database.database().reference(withPath: "kitchen")
    }
    
    func loadFood (table: Table) {        
        networkService.get(url: "/food/") { [weak self] result in
            // Other part failed
            let error = self?.errorMessage
            if error != nil {
                return
            }
            
            switch result {
                case let .success(data):
                    let decoded = try! JSONDecoder().decode([Food].self, from: data)
                    
                    DispatchQueue.main.async { [weak self] in
                        self?.firstTaskResult = decoded
                        self?.showLoadedFood()
                    }
                case let .failure(error):
                    self?.displayError(error)
            }
        }
        
        networkService.get(url: "/order-food/at/" + String(table.id), completion: { [weak self] result in
            // Other part failed
            let error = self?.errorMessage
            if error != nil {
                return
            }
            
            switch result {
                case let .success(data):
                    let decoded = try! JSONDecoder().decode([FoodOrder].self, from: data)
                    
                    DispatchQueue.main.async { [weak self] in
                        self?.secondTaskResult = decoded
                        self?.showLoadedFood()
                    }
                case let .failure(error):
                    self?.displayError(error)
            }
        })
    }
    
    private func showLoadedFood () {
        guard let firstTaskResult = firstTaskResult, let secondTaskResult = secondTaskResult else { return }
        
        // Load all items
        originalFood = [Int: FoodOrder]()
        
        // Every food should be shown
        for foodItem in firstTaskResult {
            originalFood[foodItem.id] = FoodOrder(id: nil, order: nil, food: foodItem, datetime: nil, count: 0)
        }
        // Selected food should have correct count and order on them
        for orderItem in secondTaskResult {
            originalFood[orderItem.food.id] = orderItem
        }
        
        // Set food
        food = originalFood
        firstLoaded = true
        
        // Reset results
        self.firstTaskResult = nil
        self.secondTaskResult = nil
    }
    
    func editFood (table: Table, dismiss: @escaping () -> ()) {
        let body = food.filter({$0.value.count > 0}).values.map({[
            "order": nil,
            "food": $0.food.id,
            "datetime": nil,
            "count": $0.count
        ]})
        
        networkService.post(url: "/order-food/at/" + String(table.id), body: body, completion: { [weak self, dismiss] result in
            switch result {
                case .success:
                    DispatchQueue.main.async { [dismiss] in
                        self?.sendToKitchen(table: table)
                        dismiss()
                    }
                case let .failure(error):
                    self?.displayError(error)
            }
        })
    }
    
    func sendToKitchen (table: Table) {
        let path = "Table \(table.id)"
        
        databaseReference.child(path).observeSingleEvent(of: .value, with: { [weak self] snapshot in
            guard let self = self, let value = snapshot.value else { return }
            
            // Just updating
            if var kitchenItem = try? FirebaseDecoder().decode(KitchenItem.self, from: value) {
                
                for (foodId, foodOrder) in self.originalFood.filter({$0.value.food.cooked}) {
                    if let newFoodOrder = self.food.filter({$0.value.food.cooked})[foodId] {
                        let diff = newFoodOrder.count - foodOrder.count
                        
                        // added to order
                        if diff > 0 {
                            for _ in 1 ... diff {
                                kitchenItem.food.append(foodOrder.food.name)
                            }
                        }
                        
                        // removed from order
                        // NOTE: removing food that is done = F for restaurant
                        if diff < 0 {
                            for _ in 1 ... -diff {
                                if let index = kitchenItem.food.firstIndex(of: foodOrder.food.name) {
                                    kitchenItem.food.remove(at: index)
                                }
                            }
                        }
                    }
                }
                
                // No cooked food now, remove from table
                if kitchenItem.food.isEmpty {
                    self.databaseReference.child(path).removeValue()
                }
                
                // rewrite with new data
                let data = try! FirebaseEncoder().encode(kitchenItem)
                self.databaseReference.child(path).setValue(data)
                
                print("Update, value \(value)")
            }
            // Creating new order on table
            else {
                var kitchenItem = KitchenItem(key: path, table: path, food: ["Dummy"], done: ["Dummy"], timestamp: Date.init())
                
                for (_, foodOrder) in self.food.filter({$0.value.count > 0 && $0.value.food.cooked}) {
                    for _ in 1 ... foodOrder.count {
                        kitchenItem.food.append(foodOrder.food.name)
                    }
                }
                
                // No cooked food, do not add to table
                if kitchenItem.food.isEmpty {
                    return
                }
                
                // create with new data
                let data = try! FirebaseEncoder().encode(kitchenItem)
                self.databaseReference.child(path).setValue(data)
            }
        })
    }
    
}
