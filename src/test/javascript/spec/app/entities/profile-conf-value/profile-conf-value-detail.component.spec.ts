/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CraTestModule } from '../../../test.module';
import { ProfileConfValueDetailComponent } from 'app/entities/profile-conf-value/profile-conf-value-detail.component';
import { ProfileConfValue } from 'app/shared/model/profile-conf-value.model';

describe('Component Tests', () => {
    describe('ProfileConfValue Management Detail Component', () => {
        let comp: ProfileConfValueDetailComponent;
        let fixture: ComponentFixture<ProfileConfValueDetailComponent>;
        const route = ({ data: of({ profileConfValue: new ProfileConfValue(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [ProfileConfValueDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ProfileConfValueDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProfileConfValueDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.profileConfValue).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
