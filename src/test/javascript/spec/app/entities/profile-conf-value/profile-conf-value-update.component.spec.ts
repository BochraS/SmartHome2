/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CraTestModule } from '../../../test.module';
import { ProfileConfValueUpdateComponent } from 'app/entities/profile-conf-value/profile-conf-value-update.component';
import { ProfileConfValueService } from 'app/entities/profile-conf-value/profile-conf-value.service';
import { ProfileConfValue } from 'app/shared/model/profile-conf-value.model';

describe('Component Tests', () => {
    describe('ProfileConfValue Management Update Component', () => {
        let comp: ProfileConfValueUpdateComponent;
        let fixture: ComponentFixture<ProfileConfValueUpdateComponent>;
        let service: ProfileConfValueService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [ProfileConfValueUpdateComponent]
            })
                .overrideTemplate(ProfileConfValueUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProfileConfValueUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfileConfValueService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ProfileConfValue(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.profileConfValue = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ProfileConfValue();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.profileConfValue = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
