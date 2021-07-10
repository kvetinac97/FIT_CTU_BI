//
//  FoodOrder.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import Foundation

struct FoodOrder: Identifiable, Codable {
    let id: Int?
    let order: Int?
    let food: Food
    let datetime: String?
    var count: Int
}

extension FoodOrder {
    
    static let preview = FoodOrder (
        id: 1,
        order: 1,
        food: Food.preview,
        datetime: Date.init().description,
        count: 3
    )
    
}
