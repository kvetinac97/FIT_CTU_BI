//
//  CreateViewModifier.swift
//  RestSys
//
//  Created by Thats Me on 24.01.2021.
//

import SwiftUI

struct CreateViewModifier: ViewModifier {
    
    @ObservedObject var viewModel: ViewModelCreate
    var successAction: () -> ()
    
    func body(content: Content) -> some View {
        content
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
                            successAction()
                        }
                }
            }
    }
}
