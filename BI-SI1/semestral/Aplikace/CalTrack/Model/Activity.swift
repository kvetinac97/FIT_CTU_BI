//
//  Activity.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

struct Activity : Codable, Identifiable {
    let id: Int
    let name: String
    let caloriesPerMinute: Double
    let unit: String
    let unitToMinutes: Double
}

extension Activity {
    static let preview = Activity(id: 1, name: "Běh", caloriesPerMinute: 12, unit: "min", unitToMinutes: 1)
}
