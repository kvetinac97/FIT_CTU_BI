//
//  KitchenItem.swift
//  RestSys
//
//  Created by Thats Me on 22.01.2021.
//

import Foundation

struct KitchenItem: Identifiable, Codable {
    var id: String? { key }
    var key: String? // firebase
    
    let table: String
    var food: [String] // food which is not ready
    var done: [String] // food already cooked
    let timestamp: Date
}

extension Date {

    static func - (lhs: Date, rhs: Date) -> Int {
        let newDateMinutes = lhs.timeIntervalSinceReferenceDate / 60
        let oldDateMinutes = rhs.timeIntervalSinceReferenceDate / 60
        return Int(Float(newDateMinutes - oldDateMinutes).rounded(FloatingPointRoundingRule.down))
    }

}
