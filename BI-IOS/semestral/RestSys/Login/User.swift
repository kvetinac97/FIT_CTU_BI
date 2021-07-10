//
//  User.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import Foundation

struct User: Codable {    
    let username: String
    var password: String?
    let admin: Bool
}
