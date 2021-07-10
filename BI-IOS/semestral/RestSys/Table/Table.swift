//
//  Table.swift
//  RestSys
//
//  Created by Thats Me on 20.01.2021.
//

import SwiftUI

struct Table: Identifiable, Codable {
    
    let id: Int
    let type: TableType
    let empty: Bool
    
    enum TableType: String, Codable {
        case bar = "BAR"
        case inside = "INSIDE"
        case outside = "OUTSIDE"
    }
    
    var image: Image {
        get {
            switch (type) {
                case .inside:
                    return Image(systemName: empty ? "house" : "house.fill")
                case .outside:
                    return Image(systemName: empty ? "sun.min" : "sun.max.fill")
                case .bar:
                    return Image(systemName: empty ? "cloud" : "cloud.fill")
            }
        }
    }
    
    static let preview = Table (id: 11, type: .bar, empty: false)
    
}
