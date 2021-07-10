//
//  AuthUser.swift
//  RestSys
//
//  Created by Thats Me on 20.01.2021.
//

import SwiftUI

class AuthUser: ObservableObject {
    
    private var authService: AuthServicing
    
    @Published var user: User? = nil
    @Published var loggedIn: Bool = false
    @Published var admin: Bool = false
    
    init (user: User? = nil) {
        authService = AuthService()
        if let user = authService.getUser() ?? user {
            login(user: user)
        }
    }
    
    func logout () {
        authService.logout()
        
        self.user = nil
        self.loggedIn = false
        self.admin = false
    }
    
    func login (user: User) {
        authService.setCredentials(user: user)
        
        self.user = user
        self.loggedIn = true
        self.admin = user.admin
    }
    
}
