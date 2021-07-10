//
//  LoginView.swift
//  RestSys
//
//  Created by Thats Me on 18.01.2021.
//

import SwiftUI

struct LoginView: View {
    
    @StateObject var viewModel = LoginViewModel()
    @EnvironmentObject var user: AuthUser
    
    var body: some View {
        ZStack {
            Form {
                TextFieldView(title: "Username", content: $viewModel.username)
                    .textContentType(.username)
                    .autocapitalization(.none)
                
                TextFieldView(title: "Password", content: $viewModel.password, secure: true)
                    .textContentType(.password)
                    .autocapitalization(.none)
                
                HStack {
                    Spacer()
                    Button("Login", action: viewModel.login)
                    Spacer()
                }
            }
            
            if viewModel.state == .busy {
                ProgressView()
            }
        }
        .modifier(CreateViewModifier(viewModel: viewModel, successAction: setLoggedIn))
    }
}

extension LoginView {
    func setLoggedIn () {
        user.login(user: viewModel.user!)
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
