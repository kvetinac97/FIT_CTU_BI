//
//  UserData.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

struct UserData: Codable {
    let birth: String
    let height: Int
    let weight: Int
    let goal: Int
}
