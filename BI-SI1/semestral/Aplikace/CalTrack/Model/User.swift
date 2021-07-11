//
//  User.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 04.04.2021.
//

import Foundation

struct User: Codable, Identifiable {
    let id: Int
    let birth: String
    let height: Int
    let weight: Int
    let goal: Goal
    let calories: Int
    let carbohydrates: Int
    let protein: Int
    let fat: Int
    let code: String
}

extension User {
    static let preview = User (id: 1, birth: Date().description, height: 170, weight: 80, goal: Goal.preview, calories: 2000, carbohydrates: 400, protein: 200, fat: 300, code: "1234-5678-9012-3456")
}
