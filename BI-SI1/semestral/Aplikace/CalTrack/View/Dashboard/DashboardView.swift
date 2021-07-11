//
//  DashboardView.swift
//  CalTrack
//
//  Created by Ondřej Wrzecionko on 04.04.2021.
//

import SwiftUI

struct DashboardView: View {
    @StateObject var viewModel = DashboardViewModel()
    @EnvironmentObject var user: AuthUser
    
    @State var foodTabShown       = true
    @State var isSearchedFood     = true
    @State var isInfoFoodPresented     = false
    @State var isInfoActivityPresented = false
    @State var currentMealFood = UserMealFood.preview
    @State var currentActivity = UserActivity.preview
    
    var body: some View {
        NavigationView {
            ZStack {
                Color(UIColor.darkGray)
                    .edgesIgnoringSafeArea(.all)
                
                if viewModel.error {
                    VStack {
                        Text("Při načítání nastala chyba")
                        Button("Zkusit znovu") {
                            viewModel.loadDashboard()
                        }
                    }
                }
                else if !viewModel.ready {
                    ProgressView()
                        .onAppear {
                            viewModel.loadDashboard()
                        }
                }
                else {
                    ScrollView {
                        VStack {
                            CirclesView(viewModel: viewModel, foodTabShown: $foodTabShown)
                                .padding(.top, 20)
                            if foodTabShown {
                                FoodTabView(viewModel: viewModel, foodTabShown: $foodTabShown, isInfoPresented: $isInfoFoodPresented, currentMealFood: $currentMealFood)
                            }
                            else {
                                ActivityTabView(viewModel: viewModel, foodTabShown: $foodTabShown, isInfoPresented: $isInfoActivityPresented, currentActivity: $currentActivity)
                            }
                        } // end of VStack
                        .padding(.top, 10)
                        .padding(.bottom, 50)
                    } // end of ScrollView
                    .onTapGesture {
                        isInfoFoodPresented     = false
                        isInfoActivityPresented = false
                    }
                    .opacity(isInfoFoodPresented || isInfoActivityPresented ? 0.4 : 1)
                    .navigationBarTitle(Text("Denní přehled"), displayMode: .inline)
                    .navigationBarItems(trailing: Button(action: viewModel.loadDashboard) {
                        Image(systemName: "arrow.clockwise")
                    })
                    if isInfoFoodPresented {
                        MealInfoView(userMealFood: currentMealFood, viewModel: viewModel)
                    }
                    if isInfoActivityPresented {
                        ActivityInfoView(userActivity: currentActivity, viewModel: viewModel)
                    }
                } // end of else
            } // end of ZStack
            .animation(.interactiveSpring())
        } // end of NavigatorView
    } // end of body
}

struct DashboardView_Previews: PreviewProvider {
    static var previews: some View {
        DashboardView()
    }
}

