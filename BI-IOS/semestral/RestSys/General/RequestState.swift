//
//  RequestState.swift
//  RestSys
//
//  Created by Thats Me on 18.01.2021.
//

import Foundation

enum RequestState: Equatable {
    case fresh
    case busy
    case done ( success: Bool )
}
