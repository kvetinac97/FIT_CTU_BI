//
//  Allergen.swift
//  RestSys
//
//  Created by Thats Me on 30.12.2020.
//

import Foundation

struct Allergen: Identifiable, Codable {
    let id: Int
    let name: String
    static let list = [
        "Lepek", "Korýši", "Vejce", "Ryby",
        "Arašídy", "Sója", "Mléko", "Ořechy",
        "Celer", "Hořčice", "Sezam", "Siřičitany",
        "Vlčí bob", "Měkkýši"
    ]
}

extension Allergen {
    static let preview = Allergen (
        id: 1,
        name: "Gluten"
    )
}
