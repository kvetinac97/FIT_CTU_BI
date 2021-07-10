//
//  Food.swift
//  RestSys
//
//  Created by Thats Me on 26.12.2020.
//

import Foundation

struct Food: Identifiable, Codable {
    let id: Int
    let name: String
    let price: Int
    let cooked: Bool
    let allergens: [Int]
}

extension Food {
    static let preview = Food (
        id: 1,
        name: "Kebab",
        price: 50,
        cooked: false,
        allergens: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
    )
}
