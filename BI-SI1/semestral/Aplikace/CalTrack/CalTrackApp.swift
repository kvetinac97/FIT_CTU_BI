//
//  CalTrackApp.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 02.03.2021.
//

import SwiftUI

@main
struct CalTrackApp: App {
    
    var user = AuthUser()
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(user)
        }
    }
}
