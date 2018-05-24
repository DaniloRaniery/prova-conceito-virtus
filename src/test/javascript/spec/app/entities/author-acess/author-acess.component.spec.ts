/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JHipsterBlogTestModule } from '../../../test.module';
import { AuthorAcessComponent } from '../../../../../../main/webapp/app/entities/author-acess/author-acess.component';
import { AuthorAcessService } from '../../../../../../main/webapp/app/entities/author-acess/author-acess.service';
import { AuthorAcess } from '../../../../../../main/webapp/app/entities/author-acess/author-acess.model';

describe('Component Tests', () => {

    describe('AuthorAcess Management Component', () => {
        let comp: AuthorAcessComponent;
        let fixture: ComponentFixture<AuthorAcessComponent>;
        let service: AuthorAcessService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JHipsterBlogTestModule],
                declarations: [AuthorAcessComponent],
                providers: [
                    AuthorAcessService
                ]
            })
            .overrideTemplate(AuthorAcessComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuthorAcessComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuthorAcessService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new AuthorAcess(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.authorAcesses[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
