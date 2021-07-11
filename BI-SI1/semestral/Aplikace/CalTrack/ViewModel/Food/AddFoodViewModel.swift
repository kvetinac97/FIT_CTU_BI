//
//  AddFoodViewModel.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 22.04.2021.
//

import Foundation

/**
 * Class used to log food eaten by user on a meal
 */
final class AddFoodViewModel : ObservableObject {
    
    // MARK: - Form properties
    @Published var mealId = 0
    @Published var amount = ""
    @Published var date = Date()
    
    @Published var errorMessage: HttpStatusError? = nil
    @Published var state = RequestState.fresh
    
    private let networkService: NetworkServicing
    
    // MARK: - Initialization
    init (networkService: NetworkServicing = NetworkService()) {
        self.networkService = networkService
    }
    
    // MARK: - Create
    
    /**
     * Asynchronous function to call "log food eaten" action on server
     * @param foodId id of the food eaten
     * on success/failure, corresponding properties are set (and observed by View)
     */
    func createFood (foodId: Int) {
        // All fields must be non-null
        if amount == "" {
            errorMessage = HttpStatusError.badtext(text: "Invalid data")
            state = .done (success: false)
            return
        }
        
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = "yyyy-MM-dd"
        
        let body: [String: Any?] = [
            "foodId": foodId,
            "mealId": mealId,
            "amount": amount,
            "date": dateFormatterPrint.string(from: date) + "T00:00:00.000+00:00"
        ]
        
        state = .busy
        networkService.post(url: "/user-food/", body: body) { [weak self] result in
            DispatchQueue.main.async { [weak self] in
                switch result {
                    case .success(_):
                        self?.state = .done (success: true)
                    case let .failure(error):
                        self?.errorMessage = error
                        self?.state = .done (success: false)
                }
            }
        }
    }
    
    @Published var mealList = [Meal]()
    @Published var ready = false
    @Published var error = false
    
    /**
     * Function that asynchronously loads a meal list
     */
    func loadMealList () {
        ready = false
        error = false
        
        networkService.get(url: "/meal") { [weak self] result in
            switch result {
                case let .success(data):
                    self?.loadMealListSuccess(data: data)
                case let .failure(error):
                    self?.displayError(error: error)
            }
        }
    }
    
    /**
     * Callback in case of loadMealList success
     * @param data HTTP response data containing list of Meals
     */
    func loadMealListSuccess(data: Data) {
        let decoded = try! JSONDecoder().decode([Meal].self, from: data)
        print(decoded) // debug
        
        DispatchQueue.main.async { [weak self] in
            self?.mealList = decoded
            self?.ready = true
        }
    }
    
    /**
     * Callback in case that an error has happened (after an attempt to loadMealList), setting the error flag accordingly
     * @param error helper class holding information about the error
     */
    func displayError (error: HttpStatusError) {
        print(error) // debug
        self.error = true
    }
    
}
