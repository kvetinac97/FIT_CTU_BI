//
//  ContentView.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 02.03.2021.
//

import SwiftUI

struct ContentView: View {
    
    @EnvironmentObject var user: AuthUser
    
    var body: some View {
        DashboardView()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
