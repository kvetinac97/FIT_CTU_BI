//
//  NetworkService.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 04.04.2021.
//

import Foundation

/**
 * Interface capable of handling network requests
 */
protocol NetworkServicing {
    
    /** Auth service used for authenticating requests */
    var authService: AuthServicing { get }
    
    /**
     * Perform HTTP GET method asynchronously
     * @param url to perform on
     * @param completion lambda defining success/failure reactions
     */
    func get    ( url: String, completion: @escaping (Result<Data, HttpStatusError>) -> Void )
    /**
     * Perform HTTP DELETE method asynchronously
     * @param url to perform on
     * @param completion lambda defining success/failure reactions
     */
    func delete ( url: String, completion: @escaping (Result<Data, HttpStatusError>) -> Void )
    /**
     * Perform HTTP POST method asynchronously
     * @param url to perform on
     * @param body the CREATE request body
     * @param completion lambda defining success/failure reactions
     */
    func post   ( url: String, body: Any, completion: @escaping (Result<Data, HttpStatusError>) -> Void )
    /**
     * Perform HTTP PATCH method asynchronously
     * @param url to perform on
     * @param body the CREATE request body
     * @param completion lambda defining success/failure reactions
     */
    func patch  ( url: String, body: Any, completion: @escaping (Result<Data, HttpStatusError>) -> Void )

    /**
     * Perform HTTP method asynchronously
     * @param url to perform on
     * @param method which is being called
     * @param body the request body, nil in case when request has no body
     * @param completion lambda defining success/failure reactions
     */
    func fetch  ( url: String, method: String, body: Any?, completion: @escaping (Result<Data, HttpStatusError>) -> Void)
    /**
     * Perform HTTP method asynchronously
     * @param urlRequest URL request object containing correct HTTP method, body and headers
     * @param completion lambda defining success/failure reactions
     */
    func fetch (urlRequest: URLRequest, completion: @escaping (Result<Data, HttpStatusError>) -> Void)
    
}

struct NetworkService: NetworkServicing {
    
    // MARK: Interface method implementation
    
    var authService: AuthServicing = AuthService()
    private let API_LOCATION = "http://192.168.0.108:8080"
    
    func get (url: String, completion: @escaping (Result<Data, HttpStatusError>) -> Void) {
        fetch(url: url, method: "GET", body: nil, completion: completion)
    }
    
    func delete (url: String, completion: @escaping (Result<Data, HttpStatusError>) -> Void) {
        fetch (url: url, method: "DELETE", body: nil, completion: completion)
    }
    
    func patch (url: String, body: Any, completion: @escaping (Result<Data, HttpStatusError>) -> Void) {
        fetch(url: url, method: "PATCH", body: body, completion: completion)
    }
    
    func post (url: String, body: Any, completion: @escaping (Result<Data, HttpStatusError>) -> Void) {
        fetch(url: url, method: "POST", body: body, completion: completion)
    }
    
    func fetch (url: String, method: String, body: Any?, completion: @escaping (Result<Data, HttpStatusError>) -> Void) {
        var urlRequest = URLRequest(url: URL(string: API_LOCATION + url)!)
        urlRequest.httpMethod = method
        
        if let body = body {
            urlRequest.allHTTPHeaderFields = ["Content-Type": "application/json"]
            urlRequest.httpBody = try! JSONSerialization.data(withJSONObject: body)
        }
        
        addHeaders(to: &urlRequest);
        fetch (urlRequest: urlRequest, completion: completion)
    }
    
    func fetch (urlRequest: URLRequest, completion: @escaping (Result<Data, HttpStatusError>) -> Void) {
        URLSession.shared
            .dataTask(with: urlRequest, completionHandler: { data, response, error in
                if let error = error {
                    completion(.failure(.badtext(text: error.localizedDescription)))
                    return
                }
                
                if let httpResponse = response as? HTTPURLResponse {
                    if httpResponse.statusCode < 200 || httpResponse.statusCode > 204 {
                        completion(.failure(.badcode(code: httpResponse.statusCode)))
                        return
                    }
                }
                
                guard let data = data else {
                    completion(.failure(.badtext(text: "No data from server")))
                    return
                }
                completion(.success(data))
            })
            .resume()
    }
    
    // END Interface method implementation
    
    /**
     * Inner helper function, adding information about user being logged in to request headers
     * @param request the request to which information should be added
     */
    private func addHeaders (to request: inout URLRequest) {
        if let user = authService.getUser() {
            request.addValue(user.code, forHTTPHeaderField: "Code")
        }
    }
    
}

/**
 * Helper enum class determining error whilst performing HTTP request
 */
enum HttpStatusError: Error, Identifiable, LocalizedError {
    case badcode (code: Int)
    case badtext (text: String)
    
    var id: String {
        switch self {
            case let .badcode(code: code):
                return String(code)
            case let .badtext(text: text):
                return text
        }
    }
    
    var errorDescription: String? {
        switch self {
            case let .badcode(code):
                return NSLocalizedString(
                    code == 401 ? "Invalid credentials" : "Http status error \(code)",
                    comment: ""
                )
            case let .badtext(text: text):
                return NSLocalizedString(text, comment: "")
        }
    }
}
