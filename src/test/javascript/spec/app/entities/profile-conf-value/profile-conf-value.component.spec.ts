/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CraTestModule } from '../../../test.module';
import { ProfileConfValueComponent } from 'app/entities/profile-conf-value/profile-conf-value.component';
import { ProfileConfValueService } from 'app/entities/profile-conf-value/profile-conf-value.service';
import { ProfileConfValue } from 'app/shared/model/profile-conf-value.model';

describe('Component Tests', () => {
    describe('ProfileConfValue Management Component', () => {
        let comp: ProfileConfValueComponent;
        let fixture: ComponentFixture<ProfileConfValueComponent>;
        let service: ProfileConfValueService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [ProfileConfValueComponent],
                providers: []
            })
                .overrideTemplate(ProfileConfValueComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProfileConfValueComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfileConfValueService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ProfileConfValue(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.profileConfValues[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
