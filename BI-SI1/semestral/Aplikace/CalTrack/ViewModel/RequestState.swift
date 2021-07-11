//
//  RequestState.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 19.04.2021.
//

import Foundation

// Help enum to find actual state of creation of (food, activity...)
enum RequestState: Equatable {
    case fresh
    case busy
    case done ( success: Bool )
}
