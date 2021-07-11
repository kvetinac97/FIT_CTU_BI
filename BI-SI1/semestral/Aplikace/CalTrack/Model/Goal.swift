//
//  Goal.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

struct Goal: Codable, Identifiable {
    let id: Int
    let name: String
    let coefficient: Double
}

extension Goal {
    static let preview = Goal (id: 1, name: "Hubnutí", coefficient: 0.8)
}
