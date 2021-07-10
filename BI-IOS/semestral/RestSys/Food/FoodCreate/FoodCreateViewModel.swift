//
//  FoodCreateViewModel.swift
//  RestSys
//
//  Created by Thats Me on 14.01.2021.
//

import SwiftUI

final class FoodCreateViewModel: ViewModelCreate {
    
    // MARK: - Form properties
    @Published var id: Int? = nil
    @Published var name: String = ""
    @Published var priceString: String = ""
    
    @Published var cooked: Bool = false
    @Published var allergens = IndexSet()
    
    // MARK: - Initialization
    init (food: Food? = nil, networkService: NetworkServicing = NetworkService()) {
        super.init(networkService: networkService)
        
        if let food = food {
             id = food.id
             name = food.name
             priceString = String(food.price)
             cooked = food.cooked
             allergens = IndexSet(food.allergens.map({$0 - 1}))
         }
    }
    
    // MARK: - Create / update
    func createOrUpdateFood () {
        // Init
        var url: String = "/food/"
        var method: String = "POST";
        
        // All fields must be non-null
        if name == "" || priceString == "" || priceString == "" {
            errorMessage = HttpStatusError.badtext(text: "Invalid data")
            state = .done (success: false)
            return
        }
        
        let body: [String: Any?] = [
            "name": name,
            "price": Int(priceString),
            "cooked": cooked,
            "allergens": Array<Int>(allergens).map({$0 + 1}) // different indexing
        ]
        
        if ( id != nil ) {
            url = "/food/" + String(id!)
            method = "PATCH"
        }
        
        super.executeMethod(url: url, body: body, method: method)
    }
    
}
