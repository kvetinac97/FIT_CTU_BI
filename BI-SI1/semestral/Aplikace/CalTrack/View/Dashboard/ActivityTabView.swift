//
//  ActivityTabView.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 03.05.2021.
//

import SwiftUI

struct ActivityTabView: View {
    @ObservedObject var viewModel: DashboardViewModel
    @Binding var foodTabShown: Bool
    @State var isDeleteActivityPresented: Bool = false
    @Binding var isInfoPresented: Bool
    @Binding var currentActivity: UserActivity
    
    var body: some View {
        VStack {
            HStack {
                PlusButton(
                    action: viewModel.loadDashboard,
                    isFoodTabShown: $foodTabShown
                )
                .padding()
                Spacer()
                DatePicker("Datum", selection: $viewModel.date, displayedComponents: .date)
                    .labelsHidden()
                Spacer()
                MinusButton(isDeletePresented: $isDeleteActivityPresented)
                    .padding()
            }
            .padding()
            LazyVStack() {
                ForEach(viewModel.activityList) { userActivity in
                    HStack {
                        ActivityRecordView (
                            userActivity: userActivity,
                            currentUserActivity: $currentActivity,
                            isPresented: $isInfoPresented
                        )
                        if isDeleteActivityPresented {
                            Button(action: { viewModel.deleteActivityRecord(activity: userActivity) }) {
                                ZStack {
                                    Circle()
                                        .foregroundColor(.white)
                                        .frame(width: 20, height: 20 )
                                    Image(systemName: "xmark.circle.fill")
                                        .resizable()
                                        .frame(width: 20, height: 20)
                                        .foregroundColor(Color.red)
                                }
                                .frame(width:20, height: 20)
                                .padding(.trailing)
                            }
                        }
                    }
                    .padding(.horizontal)
                }
                if viewModel.activityList.isEmpty {
                    Text("Žádné aktivity")
                        .foregroundColor(Color.gray)
                }
            }
        }
    }
}
