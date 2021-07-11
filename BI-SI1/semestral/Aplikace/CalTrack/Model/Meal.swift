//
//  Meal.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

struct Meal : Codable, Identifiable, Hashable {
    let id: Int
    let name: String
    let timeOfDay: String // Swift is not able to parse Time from JSON string
}

extension Meal {
    static let preview = Meal(id: 1, name: "Snídaně", timeOfDay: "08:00")
}
