//
//  ActivityRecordView.swift
//  CalTrack
//
//  Created by Roman Isaev on 21/04/2021.
//

import SwiftUI

struct ActivityRecordView: View {
    var userActivity: UserActivity
    @Binding var currentUserActivity: UserActivity
    @Binding var isPresented: Bool
    
    var body: some View {
        HStack {
            Button(
                action: {
                    currentUserActivity = userActivity
                    isPresented.toggle()
                    print(userActivity.activity.name)
                }
            ) {
                ZStack {
                    Circle()
                        .foregroundColor(.white)
                        .frame(width: 20, height: 20 )
                    Image(systemName: "info.circle")
                        .resizable()
                        .frame(width: 20, height: 20)
                        .foregroundColor(Color.blue)
                }
            }
            Text(userActivity.activity.name)
                .font(.system(size: 18))
                .foregroundColor(.white)
            Spacer()
            Text("\(userActivity.duration) \(userActivity.activity.unit)")
                .font(.system(size: 18))
                .foregroundColor(.white)
        }
        .padding(.horizontal)
    }
}
