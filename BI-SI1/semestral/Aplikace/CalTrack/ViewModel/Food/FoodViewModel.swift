//
//  FoodViewModel.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 21.04.2021.
//

import Foundation

/**
 * Class used to find or show lists of food
 */
final class FoodViewModel : ObservableObject {
    
    // Helper variables for food searching
    @Published var foodList   = [Food]()
    @Published var searchName = "" {
        didSet {
            loadList() // reload list on search box text change
        }
    }
    
    // Helper variables determining error / ready state
    @Published var ready = false
    @Published var error = false
    private let networkService: NetworkServicing
    let caloryService: CaloryServicing
    
    /**
     * Initiates the ViewModel with given NetworkService and CaloryService
     */
    init (networkService: NetworkServicing = NetworkService(), caloryService: CaloryServicing = CaloryService()) {
        self.networkService = networkService
        self.caloryService = caloryService
    }
    
    /**
     * Function to asynchronously load the food list
     * If the search text is empty, user food history is loaded (TOP 30 most frequently eaten food)
     * Otherwise, search of given text is performed
     */
    func loadList () {
        ready = false
        error = false
        
        let encoded = searchName.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed) ?? ""
        networkService.get(url: searchName.isEmpty || encoded.isEmpty ? "/user-food/history" : "/food/search/\(encoded)") { [weak self] result in
            switch result {
                case let .success(data):
                    self?.loadFoodListSuccess(data: data)
                case let .failure(error):
                    self?.displayError(error: error)
            }
        }
    }

    /**
     * Load food list callback in case of success
     * @param data HTTP response data containing food list
     */
    func loadFoodListSuccess(data: Data) {
        let decoded = try! JSONDecoder().decode([Food].self, from: data)
        print(decoded) // debug
        
        DispatchQueue.main.async { [weak self] in
            self?.foodList = decoded
            self?.ready = true
        }
    }
    
    /**
     * Load food list callback in case of error
     * @param error helper class holding information about the error
     */
    func displayError (error: HttpStatusError) {
        print(error) // debug
        self.error = true
    }
    
}
