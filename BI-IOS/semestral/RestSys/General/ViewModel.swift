//
//  ViewModel.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import Foundation

internal class ViewModel {
    
    // MARK: - Network service + init
    internal let networkService: NetworkServicing
    
    init (networkService: NetworkServicing = NetworkService()) {
        self.networkService = networkService
    }
    
    // MARK: - Error handling
    @Published var errorMessage: HttpStatusError? = nil
    
    internal func displayError (_ error: HttpStatusError) {
        DispatchQueue.main.async { [weak self] in
            self?.errorMessage = error
        }
        print(error)
    }
    
}
