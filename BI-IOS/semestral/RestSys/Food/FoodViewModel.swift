//
//  FoodViewModel.swift
//  RestSys
//
//  Created by Thats Me on 26.12.2020.
//

import Foundation

final class FoodViewModel: ViewModelRead<Food> {
    
    func loadFood () {
        super.loadEntities(url: "/food/")
    }
    
    func removeFood (set: IndexSet) {
        for c in set {
            let elementId = entities[c].id
            
            // API does not support batch delete
            networkService.delete(url: "/food/" + String(elementId)) { [weak self] result in
                switch result {
                    case let .failure(error):
                        self?.displayError(error)
                    case .success:
                        DispatchQueue.main.async { [weak self] in
                            self?.entities.remove(atOffsets: set)
                        }
                }
            }
        }
    }
    
}
