//
//  TableCreateViewModel.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import SwiftUI

final class TableCreateViewModel: ViewModelCreate {
    
    // MARK: - Form properties
    @Published var type: String = "BAR"
    
    func createTable () {
        super.executeMethod(url: "/tables/", body: ["type": type])
    }
    
}
