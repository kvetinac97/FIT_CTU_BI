//
//  ViewModelCreate.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import Foundation

class ViewModelCreate : ViewModel, ObservableObject {
    
    // MARK: - State property
    @Published var state: RequestState = .fresh
    
    func executeMethod (url: String, body: Any?, method: String = "POST") {
        state = .busy
        
        networkService.fetch(url: url, method: method, body: body) { [weak self] result in
            DispatchQueue.main.async { [weak self] in
                switch result {
                    case let .success(data):
                        self?.executeSuccess(data)
                        self?.state = .done (success: true)
                    case let .failure(error):
                        self?.displayError(error)
                        self?.state = .done (success: false)
                }
            }
        }
    }
    
    internal func executeSuccess(_ data: Data) {}
    
}
