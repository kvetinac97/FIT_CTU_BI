//
//  SwiftUIView.swift
//  CalTrack
//
//  Created by Roman Isaev on 20/04/2021.
//
// struktura odpovídá za zobrazování "koleček"

import SwiftUI

struct CirclesView: View {
    @ObservedObject var viewModel: DashboardViewModel
    @Binding var foodTabShown: Bool
    
    var body: some View {
        ZStack {
            Circle()
                .stroke(
                    LinearGradient(
                        gradient: Gradient( colors: [.blue, .white, .red, .blue] ),
                        startPoint: .leading,
                        endPoint: .trailing),
                    lineWidth: 4)
                .frame(width: 140, height: 140)
            Text("Celkem")
                .foregroundColor(.white)
                .bold()
                .offset(y: -30)
                .font(.system(size: 20))
            Text("\(viewModel.userDay.calories) kcal")
                .foregroundColor(.white)
                .bold()
            Text("Cíl \(viewModel.user.calories) kcal")
                .foregroundColor(.white)
                .bold()
                .offset(y: 20)
        }
        .padding(.horizontal)
        HStack {
            VStack {
                Text("Bílkoviny")
                    .foregroundColor(.white)
                    .font(.system(size: 18))
                    .bold()
                ZStack {
                    Circle()
                        .stroke(
                            LinearGradient(
                                gradient: Gradient( colors: [.blue, .white, .red, .blue] ),
                                startPoint: .topLeading,
                                endPoint: .bottomLeading),
                            lineWidth: 3)
                        .frame(width: 70, height: 70 )
                    Text("\(100*viewModel.userDay.protein/viewModel.user.protein) %")
                        .foregroundColor(.white)
                        .bold()
                }
                Text("\(viewModel.userDay.protein) g")
                    .foregroundColor(.white)
                    .bold()
                Text("cíl \(viewModel.user.protein) g")
                    .foregroundColor(.white)
                    .bold()
            }
            Spacer()
            VStack {
                Text("Sacharidy")
                    .foregroundColor(.white)
                    .font(.system(size: 18))
                    .bold()
                ZStack {
                    Circle()
                        .stroke(
                            LinearGradient(
                                gradient: Gradient( colors: [.blue, .white, .red, .blue] ),
                                startPoint: .topLeading,
                                endPoint: .bottomLeading),
                            lineWidth: 3)
                        .frame(width: 70, height: 70 )
                    Text("\(viewModel.userDay.carbohydrates*100/viewModel.user.carbohydrates) %")
                        .foregroundColor(.white)
                        .bold()
                }
                Text("\(viewModel.userDay.carbohydrates) g")
                    .foregroundColor(.white)
                    .bold()
                Text("cíl \(viewModel.user.carbohydrates) g")
                    .foregroundColor(.white)
                    .bold()
            }
            Spacer()
            VStack {
                Text("Tuky")
                    .foregroundColor(.white)
                    .font(.system(size: 18))
                    .bold()
                ZStack {
                    Circle()
                        .stroke(
                            LinearGradient(
                                gradient: Gradient( colors: [.blue, .white, .red, .blue] ),
                                startPoint: .topLeading,
                                endPoint: .bottomLeading),
                            lineWidth: 3)
                        .frame(width: 70, height: 70 )
                    Text("\(viewModel.userDay.fat*100/viewModel.user.fat) %")
                        .foregroundColor(.white)
                        .bold()
                }
                Text("\(viewModel.userDay.fat) g")
                    .foregroundColor(.white)
                    .bold()
                Text("cíl \(viewModel.user.fat) g")
                    .foregroundColor(.white)
                    .bold()
            }
        }
        .padding(.horizontal)
        
        HStack {
            Button (action: { foodTabShown = true }) {
                Text("Jídla")
                    .foregroundColor(Color.white)
                    .bold()
                    .padding()
            }
            .frame(width: 120, height: 50, alignment: .center)
            .background(
                LinearGradient(
                    gradient:   foodTabShown
                        ? Gradient(colors: [.blue, .red, .red])
                        : Gradient(colors: [.blue, .blue]),
                    startPoint: foodTabShown
                        ? .bottom
                        : .topLeading,
                    endPoint:   foodTabShown
                        ? .leading
                        : .bottomLeading
                )
            )
            .foregroundColor(Color.white)
            .cornerRadius(25.0)
            
            Spacer()
            
            Button(
                action: {
                    foodTabShown = false
                }
            ) {
                Text("Aktivity")
                    .foregroundColor(Color.white)
                    .bold()
                    .padding()
            }
            .frame(width: 120, height: 50, alignment: .center)
            .background(
                LinearGradient(
                    gradient:   !foodTabShown
                        ? Gradient(colors: [.blue, .red, .red])
                        : Gradient(colors: [.blue, .blue]),
                    //startPoint: .topLeading, endPoint: .bottomLeading
                    startPoint: !foodTabShown
                        ? .bottom
                        : .topLeading,
                    endPoint:   !foodTabShown
                        ? .leading
                        : .bottomLeading
                )
            )
            .foregroundColor(Color.white)
            .cornerRadius(25.0)
        }
        .padding(.horizontal)

    }
}

