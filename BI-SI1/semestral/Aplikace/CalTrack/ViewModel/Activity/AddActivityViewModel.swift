//
//  AddActivityViewModel.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 22.04.2021.
//

import Foundation

/**
 * Class used to log activity performed by user
 */
final class AddActivityViewModel : ObservableObject {
    
    // MARK: - Form properties
    @Published var duration = ""
    @Published var date = Date()
    
    @Published var errorMessage: HttpStatusError? = nil
    @Published var state = RequestState.fresh
    
    private let networkService: NetworkServicing
    
    // MARK: - Initialization
    init (networkService: NetworkServicing = NetworkService()) {
        self.networkService = networkService
    }
    
    /**
     * Asynchronous function to call "log activity" action on server
     * @param activityId id of the activity performed
     * on success/failure, corresponding properties are set (and observed by View)
     */
    func createActivity (activityId: Int) {
        // All fields must be non-null
        if duration == "" {
            errorMessage = HttpStatusError.badtext(text: "Invalid data")
            state = .done (success: false)
            return
        }
        
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = "yyyy-MM-dd"
        
        let body: [String: Any?] = [
            "activityId": activityId,
            "duration": duration,
            "date": dateFormatterPrint.string(from: date) + "T00:00:00.000+00:00"
        ]
        
        state = .busy
        networkService.post(url: "/user-activity/", body: body) { [weak self] result in
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
    
}
