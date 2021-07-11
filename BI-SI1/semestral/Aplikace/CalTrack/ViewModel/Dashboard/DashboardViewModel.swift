//
//  DashboardViewModel.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

/**
 * Class used to hold data displayed on dashboard views
 */
final class DashboardViewModel : ObservableObject {
    
    // MARK: - Properties
    @Published var user    = User.preview
    @Published var userDay = User.preview
    @Published var activityList = [UserActivity]()
    @Published var foodMealList = [UserMealFood]()
    @Published var mealList     = [Meal]()
    
    // Date to show information from
    @Published var date = Date() {
        didSet {
            loadDashboard()
        }
    }

    private var dateString: String {
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = "yyyy-MM-dd"
        return dateFormatterPrint.string(from: date)
    }
    
    var userLoaded  = false
    var actLoaded   = false
    var foodMLoaded = false
    var mealLoaded  = false
    
    @Published var ready = false
    @Published var error = false
    
    private let networkService: NetworkServicing
    let caloryService : CaloryServicing
    
    // MARK: - Initialization
    init (networkService: NetworkServicing = NetworkService(), caloryService: CaloryServicing = CaloryService()) {
        self.networkService = networkService
        self.caloryService  = caloryService
    }
    
    /**
     * Function used to delete food consumed record
     * @param userMeal the food being deleted
     */
    func deleteUserMealRecord (userMeal: UserMealFood) {
        networkService.delete(url: "/user-food/\(userMeal.id)") { [weak self] result in
            switch result {
                case .success(_):
                    self?.loadMealFoodList() // reload
                case let .failure(error):
                    self?.displayError(error: error)
            }
        }
    }

    /**
     * Function used to delete activity record
     * @param userMeal the activity being deleted
     */
    func deleteActivityRecord (activity: UserActivity) {
        networkService.delete(url: "/user-activity/\(activity.id)") { [weak self] result in
            switch result {
                case .success(_):
                    self?.loadActivityList() // reload
                case let .failure(error):
                    self?.displayError(error: error)
            }
        }
    }

    /**
     * Function used to reload dashboard information
     */
    func loadDashboard () {
        ready = false
        error = false
        
        loadUserInfo()
        loadActivityList()
        loadMealFoodList()
        loadMealList()
    }

    /**
     * Function that asynchronously loads user's long-term goal information (calories, carbohydrates, ...)
     */
    func loadUserInfo() {
        networkService.get(url: "/user/me") { [weak self] result in
            switch result {
                case let .success(data):
                    self?.loadUserInfoSuccess(data: data)
                case let .failure(error):
                    self?.displayError(error: error)
            }
        }
    }
    
    /**
     * Function that asynchronously loads activities which user has performed today
     */
    func loadActivityList() {
        networkService.get(url: "/user-activity/daily/\(dateString)") { [weak self] result in
            switch result {
                case let .success(data):
                    self?.loadActivityListSuccess(data: data)
                case let .failure(error):
                    self?.displayError(error: error)
            }
        }
    }
    
    /**
     * Function that asynchronously loads food that user has consumed today
     */
    func loadMealFoodList() {
        networkService.get(url: "/user-food/daily/\(dateString)") { [weak self] result in
            switch result {
                case let .success(data):
                    self?.loadMealFoodListSuccess(data: data)
                case let .failure(error):
                    self?.displayError(error: error)
            }
        }
    }
    
    /**
     * Function that asynchronously loads all available meals
     */
    func loadMealList() {
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
     * Function called every time after part of important data is loaded
     * if everyting is loaded, perform data calculation
     */
    func check () {
        if (userLoaded && actLoaded && foodMLoaded && mealLoaded) {
            calculateUserDay()
            ready = true
        }
    }
    
    /**
     * Function that finds out information about calories user burned / calory data of what user ate today
     * The gained data is then set to observable properties
     */
    func calculateUserDay() {
        let caloryData = caloryService.caloriesInFoodMealList(foodMealList: foodMealList)
        let burnedCalories = caloryService.caloriesFromActivityList(activityList: activityList)
        userDay = User(id: user.id, birth: user.birth, height: user.height, weight: user.weight, goal: user.goal, calories: caloryData.calories - burnedCalories, carbohydrates: caloryData.carbohydrates, protein: caloryData.protein, fat: caloryData.fat, code: user.code)
    }

    /**
     * Callback in case of successfull @method loadUserInfo
     * @param data HTTP response data containing list of users
     */
    func loadUserInfoSuccess(data: Data) {
        let decoded = try! JSONDecoder().decode(User.self, from: data)
        print(decoded) // debug
        
        DispatchQueue.main.async { [weak self] in
            self?.user = decoded
            self?.userLoaded = true
            self?.check()
        }
    }
    
    /**
     * Callback in case of successfull @method loadActivityList
     * @param data HTTP response data containing list of activities performed today
     */
    func loadActivityListSuccess(data: Data) {
        let decoded = try! JSONDecoder().decode([UserActivity].self, from: data)
        print(decoded) // debug
        
        DispatchQueue.main.async { [weak self] in
            self?.activityList = decoded
            self?.activityList.sort {
                $0.date < $1.date
            }
            self?.actLoaded = true
            self?.check()
        }
    }
    
    /**
     * Callback in case of successfull @method loadMealFoodList
     * @param data HTTP response data containing list of food eaten today
     */
    func loadMealFoodListSuccess(data: Data) {
        let decoded = try! JSONDecoder().decode([UserMealFood].self, from: data)
        print(decoded) // debug
        
        DispatchQueue.main.async { [weak self] in
            self?.foodMealList = decoded
            self?.foodMealList.sort {
                $0.meal.timeOfDay < $1.meal.timeOfDay
            }
            self?.foodMLoaded = true
            self?.check()
        }
    }
    
    /**
     * Callback in case of successfull @method loadMealList
     * @param data HTTP response data containing list of all meals
     */
    func loadMealListSuccess(data: Data) {
        let decoded = try! JSONDecoder().decode([Meal].self, from: data)
        print(decoded) // debug
        
        DispatchQueue.main.async { [weak self] in
            self?.mealList = decoded
            self?.mealLoaded = true
            self?.check()
        }
    }
    
    /**
     * Callback in case of failure whilst  any of @method loadUserInfo, @method loadActivityList or @method loadMealFoodList
     * @param error helper object holding information about what error happened
     */
    func displayError (error: HttpStatusError) {
        print(error) // debug

        userLoaded = false
        actLoaded = false
        foodMLoaded = false
        DispatchQueue.main.async { [weak self] in
            self?.error = true
        }
    }
    
}
