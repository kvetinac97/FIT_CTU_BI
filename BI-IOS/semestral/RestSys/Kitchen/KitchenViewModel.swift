//
//  KitchenViewModel.swift
//  RestSys
//
//  Created by Thats Me on 22.01.2021.
//

import Foundation
import FirebaseDatabase
import CodableFirebase

final class KitchenViewModel: ObservableObject {
    
    @Published var orderList = [KitchenItem]()
    @Published var history = false
    
    private var databaseReference: DatabaseReference!
    
    init () {
        databaseReference = Database.database().reference(withPath: "kitchen")
        
        databaseReference.observe(.childAdded) { [weak self] snapshot in
            guard let self = self, let value = snapshot.value else { return }
            print("Child added \(snapshot)")
            
            if let kitchenItem = try? FirebaseDecoder().decode(KitchenItem.self, from: value) {
                self.orderList.append(kitchenItem)
            }
        }
        
        databaseReference.observe(.childChanged) { [weak self] snapshot in
            guard let self = self, let value = snapshot.value else { return }
            print("Child changed \(snapshot)")
            
            if let index = self.orderList.firstIndex(where: { $0.key == snapshot.key }),
               let kitchenItem = try? FirebaseDecoder().decode(KitchenItem.self, from: value) {
                self.orderList[index] = kitchenItem
            }
        }
        
        databaseReference.observe(.childRemoved) { [weak self] snapshot in
            guard let self = self else { return }
            print("Child removed \(snapshot)")
            
            if let index = self.orderList.firstIndex(where: { $0.key == snapshot.key }) {
                self.orderList.remove(at: index)
            }
        }
    }
    
    func cookedFood (item: KitchenItem, food: String) {
        // allow to switch food between history and actual food
        var foodArray = history ? item.done : item.food
        var doneArray = history ? item.food : item.done
        
        if let index = foodArray.firstIndex(of: food) {
            foodArray.remove(at: index)
            doneArray.append(food)
        }
        
        var item = item
        item.food = history ? doneArray : foodArray
        item.done = history ? foodArray : doneArray
        
        // change data
        let data = try! FirebaseEncoder().encode(item)
        databaseReference.child(item.table).setValue(data)
    }
    
}
