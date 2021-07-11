//
//  AuthService.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 04.04.2021.
//

import Foundation

/**
 * Interface identifying a service capable of saving/loading actually logged user
 */
protocol AuthServicing {

    /**
     * Save information about the user actually logged in to database
     * @param user in case of login, the user being logged in, in case of logout, nil
     */
    func setCredentials (user: User?)

    /**
     * Remove information about logged user from database
     * Alias for setCredentials(nil)
     */
    func logout ()
    
    /**
     * Get actually logged user
     * @return logged in user or nil otherwise
     */
    func getUser() -> User?

}

struct AuthService: AuthServicing {
    
    // MARK: Interface method implementation

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
    
    // END: Interface method implementation

}
