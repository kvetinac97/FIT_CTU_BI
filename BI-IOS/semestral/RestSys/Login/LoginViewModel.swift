//
//  LoginViewModel.swift
//  RestSys
//
//  Created by Thats Me on 18.01.2021.
//

import SwiftUI

final class LoginViewModel: ViewModelCreate {
    
    // MARK: - Form properties
    @Published var username = ""
    @Published var password = ""
    @Published var user: User? = nil
    
    // MARK: - Own properties
    private let authService: AuthServicing
    
    // MARK: - Initialization
    init (authService: AuthServicing = AuthService(), networkService: NetworkServicing = NetworkService()) {
        self.authService = authService
        super.init(networkService: networkService)
    }
    
    func login () {
        // All fields must be non-null
        if username.count < 4 || username.count > 16 || password.count < 4 || password.count > 16 {
            errorMessage = HttpStatusError.badtext(text: "Invalid data")
            state = .done (success: false)
            return
        }
        
        // pre-fill for HTTP request
        authService.setCredentials(user: User(username: username, password: password, admin: false))
        super.executeMethod(url: "/employees/login/", body: nil, method: "GET")
    }
    
    override func executeSuccess (_ data: Data) {
        var decoded = try! JSONDecoder().decode(User.self, from: data)
        decoded.password = password // not set by default
        
        authService.setCredentials(user: decoded)
        user = decoded
    }
    
    override func displayError (_ error: HttpStatusError) {
        authService.logout()
        super.displayError(error)
    }

}
