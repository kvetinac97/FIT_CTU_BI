//
//  UserActivity.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

struct UserActivity : Codable, Identifiable {
    let id: Int
    let activity: Activity
    let date: String // Swift is not able to parse Date from JSON string
    let duration: Int
}

extension UserActivity {
    static let preview = UserActivity(id: 1, activity: Activity.preview, date: "12.12.2012", duration: 20)
}
