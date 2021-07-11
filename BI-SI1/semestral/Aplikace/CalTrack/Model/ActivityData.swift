//
//  ActivityData.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

struct ActivityData : Codable {
    let activityId: Int
    let date: String // Swift is not able to create JSON string from Date
    let duration: Int
}
