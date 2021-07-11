//
//  Food.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

struct Food : Codable, Identifiable {
    let id: Int
    let name: String
    let unit: String
    let unitToGrams: Double
    let fat: Int
    let carbohydrates: Int
    let protein: Int
}

extension Food {
    static let preview = Food(id: 1, name: "Kebab", unit: "ks", unitToGrams: 300, fat: 12, carbohydrates: 20, protein: 5)
}
