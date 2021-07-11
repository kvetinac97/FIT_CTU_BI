//
//  ActivityInfoView.swift
//  CalTrack
//
//  Created by Roman Isaev on 02/05/2021.
//

import SwiftUI

struct ActivityInfoRow: View {
    var name: String
    var description: String
    
    var body: some View {
        Divider()
        HStack {
            Text("\(name)")
                .bold()
            Spacer()
            Text("\(description)")
        }
        .padding(.horizontal)
    }
}

struct ActivityInfoView: View {
    var userActivity: UserActivity
    @ObservedObject var viewModel: DashboardViewModel
    
    var body: some View {
        ZStack {
            Color(UIColor.darkGray)
            RoundedRectangle(cornerRadius: 20)
                .stroke(
                    LinearGradient(
                        gradient: Gradient( colors: [.blue, .white, .red, .blue] ),
                        startPoint: .leading,
                        endPoint: .trailing),
                    lineWidth: 1.2)
                .frame(width: 330, height: 350)
            VStack {
                Text("\(userActivity.activity.name)")
                    .bold()
                    .font(.system(size: 20))
                    .padding()
                ActivityInfoRow(name: "Délka:",
                                description: "\(userActivity.duration) min")
                ActivityInfoRow(name: "Kcal/min:",
                                description: "\(userActivity.activity.caloriesPerMinute)")
                ActivityInfoRow(name: "Kcal:",
                                description: "\(viewModel.caloryService.caloriesFromActivityList(activityList: [userActivity]))")
            }
            .foregroundColor(.white)
            .padding()
        }
        .frame(width: 330, height: 350, alignment: .center)
        .cornerRadius(20)
    }
}
