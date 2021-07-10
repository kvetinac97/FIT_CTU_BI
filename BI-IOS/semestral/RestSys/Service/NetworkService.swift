//
//  NetworkService.swift
//  RestSys
//
//  Created by Thats Me on 26.12.2020.
//

import Foundation

protocol NetworkServicing {
    
    var authService: AuthServicing { get }
    
    func get    ( url: String, completion: @escaping (Result<Data, HttpStatusError>) -> Void )
    func delete ( url: String, completion: @escaping (Result<Data, HttpStatusError>) -> Void )
    func post   ( url: String, body: Any, completion: @escaping (Result<Data, HttpStatusError>) -> Void )
    func patch  ( url: String, body: Any, completion: @escaping (Result<Data, HttpStatusError>) -> Void )
    func fetch  ( url: String, method: String, body: Any?, completion: @escaping (Result<Data, HttpStatusError>) -> Void)
    
    func fetch (urlRequest: URLRequest, completion: @escaping (Result<Data, HttpStatusError>) -> Void)
    
}

struct NetworkService: NetworkServicing {
    
    var authService: AuthServicing = AuthService()
    private let API_LOCATION = "http://kvetinac97.cz:8080"
    
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
                    completion(.failure(HttpStatusError.badtext(text: error.localizedDescription)))
                    return
                }
                
                if let httpResponse = response as? HTTPURLResponse {
                    if httpResponse.statusCode < 200 || httpResponse.statusCode > 204 {
                        let error = HttpStatusError.badcode(code: httpResponse.statusCode)
                        completion(.failure(error))
                        return
                    }
                }
                
                guard let data = data else {
                    completion(.failure(HttpStatusError.badtext(text: "No data from server")))
                    return
                }
                completion(.success(data))
            })
            .resume()
    }
    
    private func addHeaders (to request: inout URLRequest) {
        if let user = authService.getUser() {
            request.addValue(user.username, forHTTPHeaderField: "Username")
            request.addValue(user.password ?? "", forHTTPHeaderField: "Password")
        }
    }
    
}
