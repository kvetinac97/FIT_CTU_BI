//
//  RestSysApp.swift
//  RestSys
//
//  Created by Thats Me on 26.12.2020.
//

import SwiftUI
import FirebaseCore

@main
struct RestSysApp: App {
    
    var user = AuthUser()
    
    init () {
        FirebaseApp.configure()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(user)
        }
    }
}
