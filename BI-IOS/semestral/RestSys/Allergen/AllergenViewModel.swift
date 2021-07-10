//
//  AllergenViewModel.swift
//  RestSys
//
//  Created by Thats Me on 30.12.2020.
//

import Foundation

final class AllergenViewModel: ViewModelRead<Allergen> {
    
    @Published var foodId: Int?
        
    init(food: Int? = nil, networkService: NetworkServicing = NetworkService()) {
        self.foodId = food
        super.init(networkService: networkService)
    }
    
    func loadAllergens () {
        guard let foodId = self.foodId else {
            self.entities.append(Allergen.preview)
            return
        }
        
        super.loadEntities(url: "/food/allergens/" + String(foodId))
    }
}

extension AllergenViewModel {
    static let preview = AllergenViewModel ()
}
