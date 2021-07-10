//
//  ViewModelRead.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import Foundation

class ViewModelRead<T: Codable> : ViewModel, ObservableObject {
    
    // MARK: - Entity list
    @Published var entities = [T]()
    @Published var firstLoaded = false
    
    func loadEntities (url: String) {
        firstLoaded = false
        errorMessage = nil
        
        networkService.get(url: url) { [weak self] result in
            switch result {
                case let .success(data):
                    self?.loadSuccess(data)
                case let .failure(error):
                    self?.displayError(error)
            }
        }
    }
    
    func loadSuccess (_ data: Data) {
        let decoded = try! JSONDecoder().decode([T].self, from: data)
        
        DispatchQueue.main.async { [weak self] in
            self?.entities = decoded
            self?.firstLoaded = true
        }
    }
    
}
