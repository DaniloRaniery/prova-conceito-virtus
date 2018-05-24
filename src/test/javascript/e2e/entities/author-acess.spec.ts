import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('AuthorAcess e2e test', () => {

    let navBarPage: NavBarPage;
    let authorAcessDialogPage: AuthorAcessDialogPage;
    let authorAcessComponentsPage: AuthorAcessComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load AuthorAcesses', () => {
        navBarPage.goToEntity('author-acess');
        authorAcessComponentsPage = new AuthorAcessComponentsPage();
        expect(authorAcessComponentsPage.getTitle())
            .toMatch(/jHipsterBlogApp.authorAcess.home.title/);

    });

    it('should load create AuthorAcess dialog', () => {
        authorAcessComponentsPage.clickOnCreateButton();
        authorAcessDialogPage = new AuthorAcessDialogPage();
        expect(authorAcessDialogPage.getModalTitle())
            .toMatch(/jHipsterBlogApp.authorAcess.home.createOrEditLabel/);
        authorAcessDialogPage.close();
    });

    it('should create and save AuthorAcesses', () => {
        authorAcessComponentsPage.clickOnCreateButton();
        authorAcessDialogPage.setRequestLoginInput('requestLogin');
        expect(authorAcessDialogPage.getRequestLoginInput()).toMatch('requestLogin');
        authorAcessDialogPage.save();
        expect(authorAcessDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AuthorAcessComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-author-acess div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AuthorAcessDialogPage {
    modalTitle = element(by.css('h4#myAuthorAcessLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    requestLoginInput = element(by.css('input#field_requestLogin'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setRequestLoginInput = function(requestLogin) {
        this.requestLoginInput.sendKeys(requestLogin);
    };

    getRequestLoginInput = function() {
        return this.requestLoginInput.getAttribute('value');
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
