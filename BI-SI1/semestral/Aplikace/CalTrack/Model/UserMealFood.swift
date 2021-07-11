//
//  UserMealFood.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

struct UserMealFood : Codable, Identifiable {
    let id: Int
    let meal: Meal
    let food: Food
    let date: String // Swift is not able to parse Date from JSON string
    let amount: Int
}

extension UserMealFood {
    static let preview = UserMealFood(id: 1, meal: Meal.preview, food: Food.preview, date: "12.12.2012", amount: 1)
}
