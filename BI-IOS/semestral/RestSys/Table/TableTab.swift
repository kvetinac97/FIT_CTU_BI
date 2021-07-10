//
//  TableTab.swift
//  RestSys
//
//  Created by Thats Me on 20.01.2021.
//

import SwiftUI

struct TableTab: View {

    var body: some View {
        NavigationView {
            TableView()
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .tabItem {
            Image(systemName: "table")
            Text("Tables")
        }
    }
    
}

struct TableTab_Previews: PreviewProvider {
    static var previews: some View {
        TableTab()
    }
}
