//
//  AddActivityView.swift
//  CalTrack
//
//  Created by Roman Isaev on 21/04/2021.
//

import SwiftUI

struct AddActivityView: View {
    @StateObject var viewModel = AddActivityViewModel()
    @State var activity: Activity

    @Binding var sheetPresented: Bool
    let action: () -> ()
    
    var body: some View {
        VStack {
            if viewModel.state == .busy {
                ProgressView()
            }
            else {
                Form {
                    HStack {
                        Text("Délka výkonu:")
                            .font(.headline)
                        
                        TextField("12", text: $viewModel.duration)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                        
                        Text(activity.unit)
                    }
                    
                    HStack {
                        Text("Den výkonu:")
                            .font(.headline)
                        
                        Spacer()
                        
                        DatePicker("Datum", selection: $viewModel.date, displayedComponents: .date)
                            .labelsHidden()
                    }
                }
                
                Button(action: { viewModel.createActivity(activityId: activity.id) }) {
                    Text("Zapsat")
                        .foregroundColor(Color.white)
                        .bold()
                }
                .frame(width: 140, height: 50, alignment: .center)
                .background(LinearGradient(gradient: Gradient(colors: [.white,.blue, .blue]), startPoint: .topLeading, endPoint: .bottomLeading))
                .foregroundColor(Color.white)
                .cornerRadius(30)
                .padding(.bottom, 30)
            }
        }
        .navigationBarTitle("Zapsat \(activity.name)", displayMode: .inline)
        .alert(item: $viewModel.errorMessage) { _ in
            Alert(
                title: Text("Error"),
                message: Text(viewModel.errorMessage!.localizedDescription),
                dismissButton: .cancel()
            )
        }
        .onReceive(viewModel.$state) { state in
            switch state {
                case .fresh:
                    break
                case .busy:
                    break
                case let .done(success):
                    if success {
                        sheetPresented = false
                        action()
                    }
            }
        }
    }
}
