//
//  AuthService.swift
//  RestSys
//
//  Created by Thats Me on 20.01.2021.
//

import Foundation

protocol AuthServicing {

    func setCredentials (user: User?)
    func logout ()
    
    func getUser() -> User?

}

struct AuthService: AuthServicing {
    
    func setCredentials (user: User?) {
        let encoder = JSONEncoder()
        let data = try? encoder.encode(user)
        UserDefaults.standard.setValue(data, forKey: "user")
    }
    
    func logout () {
        setCredentials(user: nil)
    }
    
    func getUser () -> User? {
        if let data = UserDefaults.standard.data(forKey: "user") {
            let decoder = JSONDecoder()
            return try? decoder.decode(User.self, from: data)
        }
        
        return nil
    }
    
}
