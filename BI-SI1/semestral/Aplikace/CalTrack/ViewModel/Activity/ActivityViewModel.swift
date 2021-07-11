//
//  ActivityViewModel.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 21.04.2021.
//

import Foundation

/**
 * Class used to find or show lists of activities
 */
final class ActivityViewModel : ObservableObject {
    
    // Helper variables for activitiy searching
    @Published var activityList = [Activity]()
    @Published var searchName   = "" {
        didSet {
            loadList() // reload list on search box text change
        }
    }
    
    // Helper variables determining error / ready state
    @Published var ready = false
    @Published var error = false
    
    private let networkService: NetworkServicing
    
    /**
     * Initiates the ViewModel with given NetworkService
     */
    init (networkService: NetworkServicing = NetworkService()) {
        self.networkService = networkService
    }
    
    /**
     * Function to asynchronously load the activity list
     * If the search text is empty, user activity history is loaded (TOP 30 most frequent activities)
     * Otherwise, search of given text is performed
     */
    func loadList () {
        ready = false
        error = false
        
        let encoded = searchName.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed) ?? ""
        networkService.get(url: searchName.isEmpty || encoded.isEmpty ? "/user-activity/history" : "/activity/search/\(encoded)") { [weak self] result in
            switch result {
                case let .success(data):
                    self?.loadActivityListSuccess(data: data)
                case let .failure(error):
                    self?.displayError(error: error)
            }
        }
    }

    /**
     * Load activity list callback in case of success
     * @param data HTTP response data containing activity list
     */
    func loadActivityListSuccess(data: Data) {
        let decoded = try! JSONDecoder().decode([Activity].self, from: data)
        print(decoded) // debug
        
        DispatchQueue.main.async { [weak self] in
            self?.activityList = decoded
            self?.ready = true
        }
    }
    
    /**
     * Load activity list callback in case of error
     * @param error helper class holding information about the error
     */
    func displayError (error: HttpStatusError) {
        print(error) // debug
        self.error = true
    }
    
}
