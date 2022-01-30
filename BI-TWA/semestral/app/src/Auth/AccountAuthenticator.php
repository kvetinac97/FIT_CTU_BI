<?php

namespace App\Auth;

use App\Service\EntityService\AccountService;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\Security\Http\Authenticator\AbstractLoginFormAuthenticator;
use Symfony\Component\Security\Http\Authenticator\Passport\Badge\CsrfTokenBadge;
use Symfony\Component\Security\Http\Authenticator\Passport\Badge\UserBadge;
use Symfony\Component\Security\Http\Authenticator\Passport\Credentials\PasswordCredentials;
use Symfony\Component\Security\Http\Authenticator\Passport\Passport;
use Symfony\Component\Security\Http\Authenticator\Passport\PassportInterface;
use Symfony\Component\Security\Http\Util\TargetPathTrait;

class AccountAuthenticator extends AbstractLoginFormAuthenticator {

    use TargetPathTrait;

    public const LOGIN_ROUTE = 'login';

    private UrlGeneratorInterface $urlGenerator;
    private AccountService $accountService;

    public function __construct (UrlGeneratorInterface $urlGenerator, AccountService $accountService) {
        $this->urlGenerator = $urlGenerator;
        $this->accountService = $accountService;
    }

    public function authenticate (Request $request) : PassportInterface {
        $name = $request->request->get('name', '');
        $request->getSession()->set(Security::LAST_USERNAME, $name);

        return new Passport(
            new UserBadge($name, fn($accountName) => $this->accountService->getValidAccountByName($accountName)),
            new PasswordCredentials($request->request->get('password', '')),
            [new CsrfTokenBadge('authenticate', $request->request->get('_csrf_token'))]
        );
    }

    public function onAuthenticationSuccess (Request $request, TokenInterface $token, string $firewallName) : ?Response {
        if ($targetPath = $this->getTargetPath($request->getSession(), $firewallName))
            return new RedirectResponse($targetPath);

        return new RedirectResponse($this->urlGenerator->generate('profile_show'));
    }

    protected function getLoginUrl (Request $request) : string {
        return $this->urlGenerator->generate(self::LOGIN_ROUTE);
    }

}
