//
//  HttpStatusError.swift
//  RestSys
//
//  Created by Thats Me on 18.01.2021.
//

import Foundation

enum HttpStatusError: Error {
    case badcode (code: Int)
    case badtext (text: String)
}

extension HttpStatusError: Identifiable {
    var id: String {
        switch self {
            case let .badcode(code: code):
                return String(code)
            case let .badtext(text: text):
                return text
        }
    }
}

extension HttpStatusError: LocalizedError {
    
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
