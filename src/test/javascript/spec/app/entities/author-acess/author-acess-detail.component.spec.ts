/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { JHipsterBlogTestModule } from '../../../test.module';
import { AuthorAcessDetailComponent } from '../../../../../../main/webapp/app/entities/author-acess/author-acess-detail.component';
import { AuthorAcessService } from '../../../../../../main/webapp/app/entities/author-acess/author-acess.service';
import { AuthorAcess } from '../../../../../../main/webapp/app/entities/author-acess/author-acess.model';

describe('Component Tests', () => {

    describe('AuthorAcess Management Detail Component', () => {
        let comp: AuthorAcessDetailComponent;
        let fixture: ComponentFixture<AuthorAcessDetailComponent>;
        let service: AuthorAcessService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JHipsterBlogTestModule],
                declarations: [AuthorAcessDetailComponent],
                providers: [
                    AuthorAcessService
                ]
            })
            .overrideTemplate(AuthorAcessDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuthorAcessDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuthorAcessService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new AuthorAcess(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.authorAcess).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
