//
//  AuthUser.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 04.04.2021.
//

import SwiftUI

/**
 Special class, in charge of information about the user actually logged in
 */
class AuthUser: ObservableObject {
    
    private var authService: AuthServicing
    
    @Published var user: User? = nil
    @Published var loggedIn: Bool = false
    
    /**
     * Creates the Auth service
     * @param user The user who is logged in right now, nil if nobody is logged in
     */
    init (user: User? = nil) {
        authService = AuthService()
        // for SI1 purpose, we're logging in "preview User" instead of showing login form, as we're not implementing it yet
        login(user: (authService.getUser() ?? user) ?? User.preview)
    }
    
    /**
     * Perform user logout
     */
    func logout () {
        authService.logout()
        
        self.user = nil
        self.loggedIn = false
    }
    
    /**
     * Perform user login
     * @param user The user to be logged in
     * for logout, use @method logout
     */
    func login (user: User) {
        authService.setCredentials(user: user)
        
        self.user = user
        self.loggedIn = true
    }
    
}
